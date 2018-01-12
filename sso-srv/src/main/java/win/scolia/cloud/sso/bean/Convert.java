package win.scolia.cloud.sso.bean;

/**
 * bean 的转换接口
 * @param <S>
 * @param <T>
 */
public interface Convert<S, T> {
    T convert(S s);
} 