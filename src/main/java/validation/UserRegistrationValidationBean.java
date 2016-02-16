package validation;

import db.SQLQueriesHelper;
import org.hibernate.validator.constraints.Email;

import db.DataSource;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class UserRegistrationValidationBean {
    @NotNull(message = "Login must be specified")
    private String login;

    @NotNull(message = "Password must be specified")
    @Size(min = 6, message = "Password must be at least 6 symbols length")
    private String password;

    @NotNull(message = "Password must be retyped")
    private String retypePassword;

    @NotNull(message = "Email must be specified")
    @Email
    private String email;

    private boolean isValid = false;

    public UserRegistrationValidationBean(String login, String password, String retypePassword, String email) {
        this.login = login;
        this.password = password;
        this.retypePassword = retypePassword;
        this.email = email;
    }

    public String validate() {
        isValid = false;
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator val = vf.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = val.validate(this);

        StringBuffer outputJSON = new StringBuffer("{");
        if (constraintViolations.size() > 0) {
            outputJSON.append("\"registred\":\"false\",");

            for (ConstraintViolation<Object> cv : constraintViolations) {
                String propertyName = cv.getPropertyPath().toString();

                outputJSON.append(String.format("\"%s\":\"%s\",",
                                                propertyName,
                                                cv.getMessage()));
            }
            outputJSON.delete(outputJSON.length() - 1, outputJSON.length());
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

                    while (results.next()) {
                        String attrName = results.getString("attr_name");
                        String value = results.getString("value");

                        if (value == null) {
                            continue;
                        }

                        if (attrName.equals(SQLQueriesHelper.LOGIN_ATTR)) {
                            if (value.equals(login)) {
                                outputJSON.append("\"registred\":\"false\",");
                                outputJSON.append("\"login\":\"Login already exists\"");
                                break;
                            }
                        }
                        else if (attrName.equals(SQLQueriesHelper.EMAIL_ATTR)) {
                            if (value.equals(email)) {
                                outputJSON.append("\"registred\":\"false\",");
                                outputJSON.append("\"email\":\"Email already exists\"");
                                break;
                            }
                        }
                    }

                    //if login info passed conditions in while
                    if (outputJSON.length() < 2) {
                        isValid = true;
                        outputJSON.append("\"registred\":\"true\"");
                    }
                } catch (SQLException|IOException|java.beans.PropertyVetoException e) {
                    outputJSON.append("\"registred\":\"false\",");
                    outputJSON.append("\"server\":\"Cannot connect to login server\"");
                }
                finally {
                    try {
                        connection.close();
                    }
                    catch (SQLException e) {
                        outputJSON.append("\"registred\":\"false\",");
                        outputJSON.append("\"server\":\"Cannot connect to login server\"");
                    }
                }
            }
            else {
                outputJSON.append("\"registred\":\"false\",");
                outputJSON.append("\"retypePassword\":\"Passwords must be the same\"");
            }
        }
        outputJSON.append("}");

        return outputJSON.toString();
    }

    public boolean isValid() {
        return isValid;
    }
}