package validation;

import beans.UserAccount;
import db.DataSource;
import db.SQLQueriesHelper;
import org.hibernate.validator.constraints.Email;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by Денис on 14.02.2016.
 */
public class UserUpdateValidationBean {
    private String login;

    @NotNull(message = "Необходимо указать старый пароль")
    private String oldPassword;

    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    private String newPassword;

    @Email
    private String email;

    private boolean isValid = false;

    public UserUpdateValidationBean(String login, String oldPassword, String newPassword, String email) {
        this.login = login;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.email = email;
    }

    public Hashtable<String, String> validate(UserAccount currentAccount) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator val = vf.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = val.validate(this);
        Hashtable<String, String> constraintViolationsDict = new Hashtable<>();

        if (!oldPassword.equals(currentAccount.getPassword())) {
            constraintViolationsDict.put("oldPassword", "Неправильно указан старый пароль");
        }

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Object> cv : constraintViolations) {
                String propertyName = cv.getPropertyPath().toString();

                constraintViolationsDict.put(propertyName, cv.getMessage());
            }
        }
        else {
            try (
                    Connection connection = DataSource.getInstance().getConnection();
                    Statement statement = connection.createStatement()
            ) {
                String[] types = new String[1];
                types[0] = "1";
                String[] params = new String[2];
                params[0] = SQLQueriesHelper.LOGIN_ATTR;
                params[1] = SQLQueriesHelper.EMAIL_ATTR;
                ResultSet results = statement.executeQuery(SQLQueriesHelper.selectParams(types, null, params, null));

                while (results.next()) {
                    String attrName = results.getString("attr_name");
                    String value = results.getString("value");

                    if (value == null) {
                        continue;
                    }

                    if (attrName.equals(SQLQueriesHelper.LOGIN_ATTR)) {
                        if (value.equals(login)) {
                            constraintViolationsDict.put("login", "Логин занят");
                            break;
                        }
                    } else if (attrName.equals(SQLQueriesHelper.EMAIL_ATTR)) {
                        if (value.equals(email)) {
                            constraintViolationsDict.put("email", "Такой email уже зарегистрирован");
                            break;
                        }
                    }
                }
            } catch (SQLException | IOException | java.beans.PropertyVetoException e) {
                constraintViolationsDict.put("server", "Внутренняя ошибка сервера");
            }
        }

        if (constraintViolations.size() > 0) {
            isValid = false;
        }
        else {
            isValid = true;
        }

        return constraintViolationsDict;
    }

    public boolean isValid() {
        return isValid;
    }
}