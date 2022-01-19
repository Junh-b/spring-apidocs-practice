package net.junhabaek.springapidocs.utils;

import java.lang.reflect.Field;

public interface TestUtils {
    /*
        Entity '생성'시 id가 설정되지 않은 상태로 만들어진다.
        MVC 테스트시, Service layer를 stubbing하는 경우, id가 자동 갱신되지 않으므로 명시적으로 id를 설정해야 한다.
     */
    public static <T> void setLongId(T entity, String fieldName, Long id) throws NoSuchFieldException, IllegalAccessException {
        Field idField = entity.getClass().getDeclaredField(fieldName);
        idField.setAccessible(true); // false로 재설정 불필요
        idField.set(entity, id); // setLong은 primitive type인 long필드만 넣을 수 있다.
    }
}
