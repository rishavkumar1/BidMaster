package helper;

import play.Application;

/**
 * Created by vinay on 12/06/17.
 */
public interface IHelper {

    void init(Application application);

    void destroy();

}
