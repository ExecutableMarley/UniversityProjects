def printParams(func):
    def hull(*val):
        # len varnames could be used instead
        varcount = func.__code__.co_argcount
        # tuples of variable names starting with parameters
        varnames = func.__code__.co_varnames

        for i in range(varcount):
            print("Parameter [" + varnames[i] + "] = " + str(val[i]))
            # call func and return its result
        return func(*val)
    # return hull function
    return hull


@printParams
def function1(x, y, z):
    return x + y + z


def function2(x, y, z):
    return x + y + z


# Decorations done at compile time
print("Calling function 1:")
function1(10, 20, 30)

# function2 before runtime decoration
function2(10, 20, 30)

# Decorate on runtime
function2 = printParams(function2)

# function2 after runtime decoration
print("Calling function 2:")
function2(10, 20, 30)