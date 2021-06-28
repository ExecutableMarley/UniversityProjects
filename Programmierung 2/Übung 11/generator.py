import timeit


def nth(gen, n):
    for i in range(n):
        # skip n - 1 results
        next(gen)
    # return n result
    return next(gen, -1)


def example_gen():
    # generator covers 0-99 i values
    for i in range(100):
        yield i, 2 * i


def example_list():
    # convert generator into list and return list
    return list(example_gen())


print(nth(example_gen(), 5))
print(example_list()[5])
print(list(example_gen())[5])

print(timeit.timeit(lambda: example_gen()))
print(timeit.timeit(lambda: example_list()))
print(timeit.timeit(lambda: list(example_gen())))

print(timeit.timeit(lambda: nth(example_gen(), 5)))
print(timeit.timeit(lambda: example_list()[5]))
print(timeit.timeit(lambda: list(example_gen())[5]))






