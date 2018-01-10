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
package com.happylifeplat.transaction.common.enums;

/**
 * The enum Compensation action enum.
 * @author xiaoyu
 */
public enum CompensationActionEnum {

    /**
     * Save compensate action enum.
     */
    SAVE(0,"保存"),

    /**
     * Delete compensate action enum.
     */
    DELETE(1,"删除"),

    /**
     * Update compensate action enum.
     */
    UPDATE(2,"更新"),

    /**
     * Compensate compensate action enum.
     */
    COMPENSATE(3,"补偿"),

    ;

    private int code;

    private String desc;

    CompensationActionEnum(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
