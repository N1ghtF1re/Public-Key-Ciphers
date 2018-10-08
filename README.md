<h1 align="center">Public-Key-Ciphers</h1>
<p align="center"><img src="https://i.imgur.com/ZfMSTrk.png" width=150></p>

<p align="center">
<a href="https://github.com/N1ghtF1re/Public-Key-Ciphers/stargazers"><img src="https://img.shields.io/github/stars/N1ghtF1re/Public-Key-Ciphers.svg" alt="Stars"></a>
<a href="https://github.com/N1ghtF1re/Public-Key-Ciphers/releases"><img src="https://img.shields.io/badge/downloads-4-brightgreen.svg" alt="Total Downloads"></a>
<a href="https://github.com/N1ghtF1re/Public-Key-Ciphers/releases"><img src="https://img.shields.io/github/tag/N1ghtF1re/Public-Key-Ciphers.svg" alt="Latest Stable Version"></a>
<a href="https://github.com/N1ghtF1re/Public-Key-Ciphers/blob/master/LICENSE"><img src="https://img.shields.io/github/license/N1ghtF1re/Public-Key-Ciphers.svg" alt="License"></a>
</p>
</p>

## About the library
The library contains three public key ciphers: Elgamal

## Class Elgamal: 

### Constructors: 
- Elgamal(long p, long x, long k). p - prime number, x - Private key, number of range (1; p - 1), Session key, mutually prime with p number of range (1; p - 1)

### Methods: 
- encrypt(byte[] plaintext) Encodes an array of bytes
  - One byte of source array turns into two elements of the array of ints.
     - CIPHERTEXT[i] = p^k mod g
     - CIPHERTEXT[i+1] = (y^k * PlAINTEXT[i/2]) mod p
- decrypt(byte[]) - Decodes an array of ints
  - Two elements of ciphertext array (int[]) turns into one byte of plaintext array
    - (b = ciphertext[i + 1], a =  ciphertext[i])
    - PLAINTEXT[i/2] =  ( b * (a^x)^-1 ) mod p = (b * (a^x)^(phi(p) - 1)) mod p, phi - Euler function
- getPublicKey() - return Public Key object (p, g, y)



