def printParams(func):
    def hull(*val, **kvals):
        # tuples of variable names starting with parameters
        varnames = func.__code__.co_varnames

        # iterate through non named arguments
        for i in range(len(val)):
            print("Parameter [" + varnames[i] + "] = " + str(val[i]))
        # iterate through named arguments
        for kVal in kvals.items():
            print("Parameter [" + kVal[0] + "] = " + str(kVal[1]))

            # call func and return its result
        return func(*val, **kvals)

    # return hull function
    return hull


@printParams
def function1(x, y, z, keyArg = None):
    return x + y + z


def function2(x, y, z, keyArg = None):
    return x + y + z


# Decorations done at compile time
print("Calling function 1:")
function1(10, 20, 30, keyArg="SomeText")

# function2 before runtime decoration
function2(10, 20, 30, keyArg="SomeText")

# Decorate on runtime
function2 = printParams(function2)

# function2 after runtime decoration
print("Calling function 2:")
function2(10, 20, 30, keyArg="SomeText")
