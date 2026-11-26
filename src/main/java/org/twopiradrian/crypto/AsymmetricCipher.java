package org.twopiradrian.crypto;

public class AsymmetricCipher implements CipherStrategy {

    public record PublicKey(long e, long n) {
    }

    public record PrivateKey(long d, long n) {
    }

    public record KeyPair(PublicKey publicKey, PrivateKey privateKey) {
    }

    private final PrivateKey ownPrivateKey;
    private PublicKey recipientPublicKey;

    public AsymmetricCipher(PrivateKey ownPrivateKey, PublicKey recipientPublicKey) {
        this.ownPrivateKey = ownPrivateKey;
        this.recipientPublicKey = recipientPublicKey;
    }

    public void setRecipientPublicKey(PublicKey recipientPublicKey) {
        this.recipientPublicKey = recipientPublicKey;
    }

    // Genera un par de claves RSA a partir de dos números primos p y q.
    public static KeyPair generateKeyPair(int p, int q) {
        if (isNotPrime(p) || isNotPrime(q) || p == q) {
            throw new IllegalArgumentException("p and q must be two distinct prime numbers.");
        }

        long n = (long) p * q;
        if (n <= 255 || n > 65535) {
            throw new IllegalArgumentException("n = p * q must be between 256 and 65535 for 2-byte blocks.");
        }

        // Función totiente de Euler: cantidad de enteros menores que n coprimos con n.
        long phi = (long) (p - 1) * (q - 1);

        // Buscamos un exponente público e coprimo con phi.
        long e = 3;
        while (gcd(e, phi) != 1) {
            e += 2;
        }

        // Calculamos el exponente privado d tal que (e * d) % phi == 1.
        long d = modInverse(e, phi);

        return new KeyPair(new PublicKey(e, n), new PrivateKey(d, n));
    }

    // Cifra cada byte m (0..255) como c = m^e mod n, guardado en 2 bytes.
    @Override
    public byte[] encrypt(byte[] plaintext) {
        if (recipientPublicKey == null) {
            throw new IllegalStateException("Recipient public key is required to encrypt.");
        }

        byte[] result = new byte[plaintext.length * 2];
        for (int i = 0; i < plaintext.length; i++) {
            long m = plaintext[i] & 0xFFL;
            long c = modPow(m, recipientPublicKey.e(), recipientPublicKey.n());

            // Guardamos el valor cifrado (0..65535) en 2 bytes (big-endian).
            result[i * 2] = (byte) ((c >>> 8) & 0xFF);
            result[i * 2 + 1] = (byte) (c & 0xFF);
        }
        return result;
    }

    // Reconstruye cada bloque c de 2 bytes y descifra como m = c^d mod n.
    @Override
    public byte[] decrypt(byte[] ciphertext) {
        if (ciphertext.length % 2 != 0) {
            throw new IllegalArgumentException("Asymmetric ciphertext length must be even.");
        }

        byte[] result = new byte[ciphertext.length / 2];
        for (int i = 0; i < result.length; i++) {
            long high = ciphertext[i * 2] & 0xFFL;
            long low = ciphertext[i * 2 + 1] & 0xFFL;
            long c = (high << 8) | low;

            long m = modPow(c, ownPrivateKey.d(), ownPrivateKey.n());
            result[i] = (byte) m;
        }
        return result;
    }

    // Exponenciación modular rápida: calcula (base^exp) % mod sin desbordar long.
    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }

    // Máximo común divisor (algoritmo de Euclides).
    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Busca el inverso multiplicativo modular de e módulo phi.
    private static long modInverse(long e, long phi) {
        for (long d = 1; d < phi; d++) {
            if ((e * d) % phi == 1) {
                return d;
            }
        }
        throw new IllegalStateException("No modular inverse found.");
    }

    private static boolean isNotPrime(int number) {
        if (number < 2) {
            return true;
        }
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return true;
            }
        }
        return false;
    }
}
