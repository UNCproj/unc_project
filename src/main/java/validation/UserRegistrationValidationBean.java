package validation;

import com.google.gson.JsonObject;
import db.DataSource;
import db.SQLQueriesHelper;
import org.hibernate.validator.constraints.Email;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class UserRegistrationValidationBean {
    @NotNull(message = "Необходимо указать логин")
    private String login;

    @NotNull(message = "Необходимо указать пароль")
    @Size(min = 6, message = "Пароль должен быть не короче 6 символов")
    private String password;

    @NotNull(message = "Необходимо повторно ввести пароль")
    private String retypePassword;

    @NotNull(message = "Необходимо указать email")
    @Email(message = "Неверный формат email")
    private String email;

    private boolean isValid = false;

    public UserRegistrationValidationBean(String login, String password, String retypePassword, String email) {
        this.login = login;
        this.password = password;
        this.retypePassword = retypePassword;
        this.email = email;
    }

    public JsonObject validate() {
        isValid = false;
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator val = vf.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = val.validate(this);

        JsonObject outputJSON = new JsonObject();

        if (constraintViolations.size() > 0) {
            outputJSON.addProperty("registred", false);

            for (ConstraintViolation<Object> cv : constraintViolations) {
                String propertyName = cv.getPropertyPath().toString();
                outputJSON.addProperty(propertyName, cv.getMessage());
            }
        }
        else {
            if (password.equals(retypePassword)) {
                Connection connection = null;
                try {
                    connection = DataSource.getInstance().getConnection();

                    Statement statement = connection.createStatement();
                    String[] types = new String[1];
                    types[0] = "1";
                    String[] params = new String[2];
                    params[0] = SQLQueriesHelper.LOGIN_ATTR;
                    params[1] = SQLQueriesHelper.EMAIL_ATTR;
                    ResultSet results = statement.executeQuery(SQLQueriesHelper.selectParams(types, null, params, null));

                    //checking attributes
                    while (results.next()) {
                        String attrName = results.getString("attr_name");
                        String value = results.getString("value");

                        if (value == null) {
                            continue;
                        }

                        if (attrName.equals(SQLQueriesHelper.LOGIN_ATTR)) {
                            if (value.equals(login)) {
                                outputJSON.addProperty("registred", false);
                                outputJSON.addProperty("login", "Логин занят");
                                break;
                            }
                        }
                        else if (attrName.equals(SQLQueriesHelper.EMAIL_ATTR)) {
                            if (value.equals(email)) {
                                outputJSON.addProperty("registred", false);
                                outputJSON.addProperty("email", "Email уже зарегистрирован");
                                break;
                            }
                        }
                    }

                    //if attributes in while checked
                    if (outputJSON.entrySet().size() == 0) {
                        isValid = true;
                        outputJSON.addProperty("registred", true);
                    }
                } catch (SQLException|IOException|java.beans.PropertyVetoException e) {
                    outputJSON.addProperty("registred", false);
                    outputJSON.addProperty("server", "Ошибка соединения с сервером авторизации");
                }
                finally {
                    try {
                        connection.close();
                    }
                    catch (SQLException e) {
                        outputJSON.addProperty("registred", false);
                        outputJSON.addProperty("server", "Ошибка соединения с сервером авторизации");
                    }
                }
            }
            else {
                outputJSON.addProperty("registred", false);
                outputJSON.addProperty("retypePass", "Пароли не совпадают");
            }
        }

        return outputJSON;
    }

    public boolean isValid() {
        return isValid;
    }
}