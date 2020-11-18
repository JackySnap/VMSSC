package nz.snapitgroup.vmssc

import android.bluetooth.BluetoothAdapter
import android.util.Base64

class LoginDetail (val loginAddress: String){
    var loginName = ""
        private set
    var loginSecurityCode = ""
        private set

    init{
        setLoginSc(loginAddress)
    }
    fun setLoginSc(loginAddress: String) {
        loginSecurityCode = setSecurityCode(loginAddress)
    }

    fun getSc() : String = loginSecurityCode
    /**
     * Calculate SC (Security Code) from last six
     * hex number of given BLE name
     */
    private fun setSecurityCode(vmsAddress: String): String {
//        val vmsAddress_ = vmsAddress.replace(":", "")
        val hex = vmsAddress
        val XORKey = "524558" //REX
        val result = xorHexString(hex, XORKey)
        return base64Encode(result)
    }

    /**
     * Encode ASCII characters to base64 Characters
     */
    private fun base64Encode(hex: String): String {
        return Base64.encodeToString(
            hexStringToByteArray(hex),
            Base64.NO_WRAP
        )
    }

    /**
     * Convert Hex in String representation to ByteArray
     */
    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4)
                    + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    override fun toString(): String {
        return "LoginDetail{" +
                "loginName='" + loginName + '\'' +
                ", loginSecurityCode='" + loginSecurityCode + '\'' +
                ", loginAddress='" + loginAddress + '\'' +
                '}'
    }

    companion object {
        /**
         * Xor operation for HEX number (in String form) encoding
         */
        fun xorHexString(hex: String, hex1: String): String {
            val sb = StringBuilder()
            val temp = StringBuilder()

            //49204c6f7665204a617661 split into two characters 49, 20, 4c...
            var i = 0
            while (i < hex.length - 1) {

                //grab the hex in pairs
                val output = hex.substring(i, i + 2)
                val output1 = hex1.substring(i, i + 2)
                //convert hex to decimal
                val decimal = output.toInt(16)
                val decimal1 = output1.toInt(16)
                //xor operation
                val result = decimal xor decimal1
                //convert the decimal to character
                if (result < 16) {
                    sb.append("0")
                }
                sb.append(Integer.toHexString(result))
                temp.append(result)
                i += 2
            }
            return sb.toString()
        }
    }
}