package backprop;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class BackPropThread implements java.lang.Runnable {
    BackPropNet bpn;
    boolean isTraining; //
    public BackPropThread() {

    }

    public void init() {
        bpn = new BackPropNet();
    }
    public void run() {

    }
    public void stop() {
        bpn = null;
    }
}
