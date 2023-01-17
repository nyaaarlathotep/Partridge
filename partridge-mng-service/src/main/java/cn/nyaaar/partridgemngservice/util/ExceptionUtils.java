/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.nyaaar.partridgemngservice.util;


import cn.nyaaar.partridgemngservice.common.enums.error.CodeEnum;
import cn.nyaaar.partridgemngservice.exception.BaseException;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class ExceptionUtils {
    private static final String PATTERN_DOUBLE_CODE_REGEX = "(\\d{3}-\\w+).*";
    private static final Pattern PATTERN_DOUBLE_CODE = Pattern.compile(PATTERN_DOUBLE_CODE_REGEX);
    private static final String CODE_SPLIT = "-";

    public static String message(String code, String subCode) {
        return code + '-' + subCode;
    }

    public static boolean isCode(String message) {
        return StringUtils.hasLength(message) && message.matches(PATTERN_DOUBLE_CODE_REGEX);
    }

    public static String[] extractCode(String message) {
        if (isCode(message)) {
            Matcher matcher = PATTERN_DOUBLE_CODE.matcher(message);
            if (matcher.find()) {
                return matcher.group(1).split(CODE_SPLIT);
            }
        }
        throw new BaseException(CodeEnum.ERROR);
    }

    public static void throwIfFatal(@NonNull Throwable t) {
        // values here derived from https://github.com/ReactiveX/RxJava/issues/748#issuecomment-32471495
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError) t;
        } else if (t instanceof ThreadDeath) {
            throw (ThreadDeath) t;
        } else if (t instanceof LinkageError) {
            throw (LinkageError) t;
        }
    }
}
