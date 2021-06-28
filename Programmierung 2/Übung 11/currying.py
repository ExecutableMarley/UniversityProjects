

def curried_add(n):
    # define new function which adds an new parameter to n
    def insideFunc(x):
        return n + x
    # return new function
    return insideFunc


def curried_add_lambda(n):
    # return lambda which adds an new parameter to n
    return lambda x: n + x


def add(x, n):
    return x + n


print(add(2, 3))
# should be 5

add_2 = curried_add(2)
print(add_2(3))
# should be 5

print(curried_add(2)(3))
# should be 5

print(curried_add_lambda(2)(3))
# should be 5
