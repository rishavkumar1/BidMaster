package helper;

import org.junit.After;
import org.junit.Before;
import play.Logger;
import play.test.WithApplication;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HelperTest extends WithApplication {

    private List<IHelper> mHelpers = new ArrayList<>();

    private <T extends IHelper> T init(Class<T> helperClass) {
        T helper = app.injector().instanceOf(helperClass);
        try {
            helper.init(app);
        } catch(Exception ex) {
            helper.destroy();
            throw new IllegalStateException("failed to initialize helper " + helper + ", destroying it!!", ex);
        }
        mHelpers.add(helper);

        return helper;
    }

    void initInjectedFields(Class caller) throws IllegalAccessException {
        for(Field field  : caller.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class) ||
                    field.isAnnotationPresent(com.google.inject.Inject.class)) {
                Class<?> clazz = field.getType();
                boolean isAccessible = field.isAccessible();
                if(!isAccessible) {
                    field.setAccessible(true);
                }
                try {
                    if(IHelper.class.isAssignableFrom(clazz)) {
                        field.set(this, init((Class<? extends IHelper>) clazz));
                    } else {
                        field.set(this, app.injector().instanceOf(field.getType()));
                    }
                } catch (Exception exception) {
                    Logger.error("Injection exception in test: ", exception);
                }

                if(!isAccessible) {
                    field.setAccessible(false);
                }
            }
        }
    }

    @Before
    public void a0_initInjectedFields() throws IllegalAccessException {
        Class caller = getClass();
        while(caller != null && HelperTest.class.isAssignableFrom(caller)){
            initInjectedFields(caller);
            caller = caller.getSuperclass();
        };
    }

    @After
    public void a0_destroy() {
        for (IHelper helper : mHelpers) {
            helper.destroy();
        }
        mHelpers.clear();
    }

}
