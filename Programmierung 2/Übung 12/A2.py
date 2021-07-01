import numpy as np
import matplotlib.pyplot as plt

# Init 2d array 
zeroArray = np.zeros((100, 100))

# Points
zeroArray[2: 100: 5, 5: 100: 10] = 1

# Horizontal lines
zeroArray[0: 100: 5, ::] = 1

# Vertical lines
zeroArray[::, 0: 100: 10] = 1

plt.imshow(zeroArray)

plt.show()

