package com.laraymond.hybird.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PtCloud {
    private List<Vector3D> vector3d;
    private long epochTime;

    public PtCloud(List<Vector3D> vector3d, long epochTime) {
        this.vector3d = vector3d;
        this.epochTime = epochTime;
    }

    public List<Vector3D> getVector3D() {
        return vector3d;
    }

    public long getEpochTime() {
        return epochTime;
    }

    @Override
    public String toString() {
        Date date = new Date(epochTime / 1000000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss:SS");
        String formattedDate = sdf.format(date);
        return "" + formattedDate;
    }

    /**
     * Print a 3d point cloud file with the headers
     *
     * @return
     */
    public String toLine() {
        StringBuilder result = new StringBuilder();
        result.append("Version 0.7");
        result.append("\n");
        result.append("FIELDS x y z rgb");
        result.append("\n");
        result.append("SIZE 4 4 4 4");
        result.append("\n");
        result.append("TYPE F F F F");
        result.append("\n");
        result.append("COUNT 1 1 1 1");
        result.append("\n");
        result.append("WIDTH 1");
        result.append("\n");
        result.append("HEIGHT ").append(getVector3D().size());
        result.append("\n");
        result.append("VIEWPOINT 0 0 0 1 0 0 0");
        result.append("\n");
        result.append("POINTS ").append(getVector3D().size());
        result.append("\n");
        result.append("ATA ascii");
        result.append("\n");


        //result.append(getEpochTime());
        //result.append(" ");
        for (int i = 0; i < getVector3D().size(); i++) {
            Vector3D v = getVector3D().get(i);
            result.append(v.getX());
            result.append(" ");
            result.append(v.getY());
            result.append(" ");
            result.append(v.getZ());
            result.append(" ");
        }
        return result.toString();
    }
}

