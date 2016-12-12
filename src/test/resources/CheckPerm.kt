class CheckPerm {
    /**
     * @param str1 first string to be checked for permutation match
     * *
     * @param str2 second string to be checked for permutation match
     * *
     * @return  Indicate if one string is a permutation of another
     */
    fun permutation(str1: String, str2: String): Boolean {
        if (str1.length != str2.length) return false

        var toCheck = StringBuilder(str1)

        for (str in str2.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val index = toCheck.indexOf(str)
            if (index == -1) {
                return false
            } else {
                toCheck = toCheck.replace(index, index + 1, "")
            }
        }

        return true
    }
}