
import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;

public class SensorFusion {

    private static final Quaternion q = new Quaternion(0, 0, 0, 0);
    //private static final ArrayList<Quaternion> qList= new ArrayList<Quaternion>();
    //private static final ArrayList<Vector3D> vList = new ArrayList<Vector3D>();
    private static final Vector3D v = new Vector3D(0, 0, 0);
    private int counterIMU = 0;

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

}
