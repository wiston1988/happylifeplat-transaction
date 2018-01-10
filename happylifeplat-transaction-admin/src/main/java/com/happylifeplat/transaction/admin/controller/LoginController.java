/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.happylifeplat.transaction.admin.controller;

import com.happylifeplat.transaction.admin.annotation.Permission;
import com.happylifeplat.transaction.admin.dto.UserDTO;
import com.happylifeplat.transaction.admin.service.LoginService;
import com.happylifeplat.transaction.common.holder.httpclient.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 * @version 1.0
 * @date 2017/10/20 10:11
 * @since JDK 1.8
 */
@RestController
public class LoginController {


    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    public AjaxResponse login(@RequestBody UserDTO userDTO) {
        final Boolean login = loginService.login(userDTO.getUserName(), userDTO.getPassword());
        return AjaxResponse.success(login);
    }

    @PostMapping("/logout")
    public AjaxResponse logout() {
        return AjaxResponse.success(loginService.logout());
    }
    
}
