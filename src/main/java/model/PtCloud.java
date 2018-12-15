package model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.List;


public class PtCloud {
    private List<Vector3D> vector3d;
    private long epochTime;

    public PtCloud(List<Vector3D> vector3d, long epochTime) {
        this.vector3d = vector3d;
        this.epochTime = epochTime;
    }

    @Override
    public String toString() {
        return "" + epochTime + " " + vector3d.subList(vector3d.size()-12,vector3d.size()).toString();
    }
}

