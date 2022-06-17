package org.wrh.cloud.common.dto;

import lombok.Data;
import org.wrh.cloud.common.enums.ResultEnum;

/**
 * @author wuruohong
 * @date 2022-06-17 9:46
 */
@Data
public class ReturnResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public ReturnResult() {

    }

    public ReturnResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 这才是一个真正的泛型方法。
     * 首先在public与返回值之间的<T>必不可少，这表明这是一个泛型方法，并且声明了一个泛型T
     * 这个T可以出现在这个泛型方法的任意位置.
     * 泛型的数量也可以为任意多个
     *    如：public <T,K> K showKeyName(Generic<T> container){
     *        ...
     *        }
     *        原文链接：https://blog.csdn.net/s10461/article/details/53941091
     *
     *        /**
     *      * 如果在类中定义使用泛型的静态方法，需要添加额外的泛型声明（将这个方法定义成泛型方法）
     *      * 即使静态方法要使用泛型类中已经声明过的泛型也不可以。
     *      * 如：public static void show(T t){..},此时编译器会提示错误信息：
     *           "StaticGenerator cannot be refrenced from static context"
     *
     */
     public static <T> ReturnResult<T> success() {
        ReturnResult<T> result = new ReturnResult<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        return result;
    }

    public static <T> ReturnResult<T> success(T data) {
        ReturnResult<T> result = new ReturnResult<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> ReturnResult<T> fail(ResultEnum resultEnum) {
        ReturnResult<T> result = new ReturnResult<>();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        return result;
    }

    public static <T> ReturnResult<T> fail(Integer code, String msg) {
        ReturnResult<T> result = new ReturnResult<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
