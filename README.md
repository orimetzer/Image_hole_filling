# Image_hole_filling
This Java package enables filling holes in images.<br>
The library support filling holes in grayscale images, where each pixel value is a
float in the range [0, 1], and hole (missing) values which are marked with the value -1.<br>
For each pixel hole in The H set, it's value is calculated based on it's boundaries, which are pixels in the B set and on the algorithm: 
![image](https://github.com/user-attachments/assets/b09190f0-6667-4829-ab90-8aaff5d33a3f)
