How to run
---

- In the terminal, navigate to the project root folder
- run 'java -jar sensor-all-1.0-SNAPSHOT.jar'

the JAR will look for the 'points_file.xyz' and 'imu.wxyz' and generate a file result of the fusion between the two sensors synchronized. 

The output will be saved in a file named 'ptcloud-rotated-yyyy-mm-dd-HH:mm:ss'

(There´s already a rotated result in the root folder).


Synchronization process
---

Based on the method used in [1] to synchronise two sensors with different frequency and clock, we define synchronisation startegy. In our case the clock source is the same. So the purpose is to find the closest IMU time to the ptCloud generated at time t (see figure 1 without need of a sync buffer message).
To select the closest time, we read each line of the imu file until the time is higher than the ptCloud generated. We compare the time difference of this last time with the ptCloud as well as the previous one and select the IMU with the smallest time difference. However those two times are used in the comparison only if they are within a timing window that is the time of the ptcloud +- 50 ms (since the scanner is at 10hz and the imu at 20hz). Another condition was to save, if not used for the current ptcloud, the time of the IMU happening imediately after. Indeed it can be used for the next ptCloud. This process is repeated for each ptCloud

<img width="816" alt="image" src="https://user-images.githubusercontent.com/30264908/50055418-8c053200-0146-11e9-891b-9f32c784f166.png">


Rotation
---
The rotation of the 3d point cloud is based on the formula in [2] using the corresponding IMU quaternion to transform the current 3d point cloud.



References
---
[1] Sa, Inkyu, et al. "Build your own visual-inertial odometry aided cost-effective and open-source autonomous drone." arXiv preprint arXiv:1708.06652 (2017).
[2] Kumar, G. Ajay, et al. "A LiDAR and IMU integrated indoor navigation system for UAVs and its application in real-time pipeline classification." Sensors 17.6 (2017): 1268.
