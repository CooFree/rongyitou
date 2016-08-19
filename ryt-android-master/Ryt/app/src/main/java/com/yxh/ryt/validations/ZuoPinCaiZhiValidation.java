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

import android.content.Context;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationExecutor;

import java.util.regex.Pattern;

/**
 * @Description: 用户名校验
 * @author
 * @date 2014-11-21 下午9:43:39 
 * @version V1.0   
 * 
 */
public class ZuoPinCaiZhiValidation extends ValidationExecutor {

	@Override
	public boolean doValidate(Context context, String text) {
		if(text.isEmpty()){
			ToastUtil.showShort(AppApplication.getSingleContext(), "材质不能为空！");
			return false;
		}
		String regex = "^\\w{0,20}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		if (!result) {
			ToastUtil.showShort(AppApplication.getSingleContext(), "材质最多输入20个字符！");
			return false;
		}
		return true;

	}

}
