package com.infoshareacademy.jbusters.web.user;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.infoshareacademy.jbusters.authentication.PasswordHashing;
import com.infoshareacademy.jbusters.dao.UserDao;
import com.infoshareacademy.jbusters.freemarker.TemplateProvider;
import com.infoshareacademy.jbusters.model.User;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@WebServlet(urlPatterns = "/add-user")
public class AddUserServlet extends HttpServlet {


    private static final String TEMPLATE_SUCESS = "confirm-registration-sucess";
    private static final String TEMPLATE_FAILED = "confirm-registration-failed";
    private static final Logger LOG = LoggerFactory.getLogger(AddUserServlet.class);

    @Inject
    private UserDao userDao;

    @Inject
    private TemplateProvider templateProvider;

    @Inject
    PasswordHashing passwordHashing;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Content-Type", "text/html; charset=utf-8");

        PrintWriter out = resp.getWriter();
        final User u = new User();

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String password2 = req.getParameter("password2");
        List<User> listUsers = userDao.findAll();
        List<User> emailList = listUsers.stream()
                .filter(e -> e.getUserEmail().equals(email))
                .collect(Collectors.toList());

        setDataTemplate(req, out, u, email, password, password2, emailList);
    }

    private void setDataTemplate(HttpServletRequest req, PrintWriter out, User u, String email, String password, String password2, List<User> emailList) throws IOException {

        Map<String, Object> model = new HashMap<>();

        Template template = templateProvider.getTemplate(getServletContext(), TEMPLATE_SUCESS);

        if (emailList.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(password2)) {

            String name = req.getParameter("name");
            String surname = req.getParameter("surname");
            u.setUserEmail(email);
            u.setUserPassword(passwordHashing.generateHash(password));
            u.setUserName(name);
            u.setUserSurname(surname);
            u.setUserRole(2);

            try {
                userDao.save(u);
                model.put("user", u);

                try {
                    template.process(model, out);
                    LOG.info("confirm-registration!! all ok");
                } catch (TemplateException e) {
                    LOG.error("Failed confirm-registration!!");
                }

            }  catch (Exception e) {
                LOG.error("Failed to add new user due to: {}", e.getMessage());
                template = templateProvider.getTemplate(getServletContext(), TEMPLATE_FAILED);

                try {
                    template.process(model, out);
                    LOG.info("Login failed!");
                } catch (TemplateException er) {
                    LOG.error("Failed confirm-registration due to {}", er.getMessage());
                }
            }
        } else {
            template = templateProvider.getTemplate(getServletContext(), TEMPLATE_FAILED);
            try {
                template.process(model, out);
                LOG.info("Login failed!");
            } catch (TemplateException er) {
                LOG.error("Failed confirm-registration due to {}", er.getMessage());
            }
        }
    }
}

