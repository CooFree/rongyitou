/**
 *  Copyright 2014 ken.cai (http://www.shangpuyun.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */
package com.yxh.ryt.validations;

import java.util.regex.Pattern;

import android.content.Context;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationExecutor;

/**
 * @Description: 用户名校验
 * @author
 * @date 2014-11-21 下午9:43:39 
 * @version V1.0   
 * 
 */
public class UserNameValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		if(text.isEmpty()){
			ToastUtil.showShort(AppApplication.getSingleContext(), "账号不能为空！");
			return false;
		}
		String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,2,5-9]))\\d{8}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		if (!result) {
			ToastUtil.showShort(AppApplication.getSingleContext(), "手机格式不正确！");
			return false;
		}
		return true;

	}

}
