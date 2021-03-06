package com.springapp.mvc.controllers;

import com.springapp.mvc.exceptions.AuthorizationException;
import com.springapp.mvc.exceptions.UserException;
import com.springapp.mvc.json_protocol.JSONResponse;
import com.springapp.mvc.model.User;
import com.springapp.mvc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Vlad on 08.07.2014.
 */
@Controller
@Transactional
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private OrderService orderService;



    @RequestMapping(value = "/api/users/get", method = RequestMethod.GET)
    public
    @ResponseBody
    JSONResponse getUsersList(HttpServletRequest request, HttpServletResponse response)
    {
        List<User> users;
        try
        {
            authorizationService.checkAccess(UserRoles.ADMIN, request.getSession());
            users = userService.getUsers();
        }
        catch (AuthorizationException ex)
        {
            return responseService.errorResponse("authorization.access_denied", "error");
        }
        return responseService.successResponse(users);
    }


    @RequestMapping(value = "/api/users/authorize", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONResponse authorizationUserJson(@RequestBody User user, HttpServletRequest request, HttpServletResponse response){

        User loginedUser;
        User authorizedUser;

        try
        {
            loginedUser = userService.loginUser(user.getEmail(), user.getPassword());
            authorizedUser = authorizationService.authorizeUser(loginedUser, request.getSession(), response);
        }
        catch (UserException ex)
        {
            return responseService.errorResponse("authorization.failed", "error");
        }
        catch (AuthorizationException ex)
        {
            return responseService.errorResponse("authorization.failed", "error");
        }

        return responseService.successResponse(authorizedUser);
    }


    @RequestMapping(value = "/api/users/get_authorized", method = RequestMethod.GET)
    public
    @ResponseBody
    JSONResponse getAuthorizedUserJson(HttpServletRequest request, HttpServletResponse response)
    {

        User authorizedUser;

        try
        {
            //loginedUser = userService.loginUser(user.getEmail(), user.getPassword());
            //authorizedUser = authorizationService.authorizeUser(loginedUser, request.getSession(), response);
            authorizedUser = authorizationService.getActiveUser(request.getSession());
        }
        catch (AuthorizationException ex)
        {
            return responseService.errorResponse("authorization.failed", "error");
        }

        return responseService.successResponse(authorizedUser);
    }


}
