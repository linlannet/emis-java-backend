/*
 * emis-core - core核心模块
 * Copyright © 2020-2025 Linlan (open@linlan.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.linlan.emis.core.security.mfa.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import net.linlan.emis.support.exception.SupportException;

/**
 * 无效的 Mfa 提供商异常
 *
 * @author Linlan
 */
public class InvalidMfaProviderException extends SupportException {

    public final Logger logger = LoggerFactory.getLogger(InvalidMfaProviderException.class);

    public InvalidMfaProviderException(String message) {
        super("invalid_mfa_provider", message, HttpStatus.UNPROCESSABLE_ENTITY);
        logger.debug("MfaProvider validation error. " + message);
    }
}
