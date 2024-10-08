package com.ktm.ksurvey.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


object CryptoUtil {

    private const val KEY_ALIAS = "KSurvey_Token"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE

    private fun getOrCreateSecretKey(): SecretKey {
        // try to load SecretKey
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(KEY_ALIAS, null)?.let { return it as SecretKey }

        // create new SecretKey
        val paramsBuilder = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(256)
        }
        val keyGenerator = KeyGenerator.getInstance(
            ENCRYPTION_ALGORITHM,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(paramsBuilder.build())
        return keyGenerator.generateKey()
    }

    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    fun encrypt(plainText: String): Pair<String, String> {
        return kotlin.runCatching {
            val cipher = getCipher()
            val secretKey = getOrCreateSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val cipherTextBytes = cipher.doFinal(plainText.toByteArray(charset("UTF-8")))
            Pair(
                Base64.encodeToString(cipherTextBytes, Base64.DEFAULT),
                Base64.encodeToString(cipher.iv, Base64.DEFAULT)
            )
        }.getOrDefault(Pair("", ""))
    }

    fun decrypt(encryptedPair: Pair<String, String>): String {
        return kotlin.runCatching {
            val cipher = getCipher()
            val secretKey = getOrCreateSecretKey()
            val cipherTextBytes = Base64.decode(encryptedPair.first, Base64.DEFAULT)
            val cipherIvBytes = Base64.decode(encryptedPair.second, Base64.DEFAULT)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, cipherIvBytes))
            val plainTextBytes = cipher.doFinal(cipherTextBytes)
            return String(plainTextBytes, charset("UTF-8"))
        }.getOrDefault("")
    }
}