# Generador de numeros aleatorios basado en una semilla
# Formula modificada del metodo congruencial lineal (LCG):
# en vez de X(n+1) = (a*Xn + c) mod m, le agrego "+ n*w"
# para que no se repita tan facil

semilla = 3
a = 17
c = 9
w = 5
m = 97
cantidad = 20

x = semilla
numeros = []

for n in range(1, cantidad + 1):
    x = (a * x + c + n * w) % m
    numeros.append(x)

print("Numeros generados:")
print(numeros)