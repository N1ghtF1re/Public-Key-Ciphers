package men.brakh.publicKeyCiphersFrame;

public class BytesConverter {
    final int limit = 1000;

    public String intArray2String(int[] arr) {
        final String elementFormat = "%d ";
        StringBuilder result = new StringBuilder();

        int count = arr.length > limit ? limit : arr.length;

        for(int i = 0; i < count; i++) {
            result.append(String.format(elementFormat,  Integer.toUnsignedLong(arr[i])));
        }
        if(limit == count) {
            result.append("...");
        }
        return result.toString();
    }

    public String byteArray2String(byte[] arr) {
        final String elementFormat = "%d ";
        StringBuilder result = new StringBuilder();

        int count = arr.length > limit ? limit : arr.length;

        for(int i = 0; i < count; i++) {
            result.append(String.format(elementFormat,  Byte.toUnsignedInt(arr[i])));
        }
        if(limit == count) {
            result.append("...");
        }
        return result.toString();
    }
}
