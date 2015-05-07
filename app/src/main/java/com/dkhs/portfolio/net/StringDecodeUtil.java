package com.dkhs.portfolio.net;

public class StringDecodeUtil {
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public static String fromUnicode(String str) {
    	//因为JSON字符串中自带转换字符，被下面方法过虑掉了，故暂时屏蔽此方法，如有问题在行处理，此方法是将unicode转义成中文编码
        return str;//fromUnicode(str.toCharArray(), 0, str.length(), new char[1024]);

    }

    /*
     * 
     * Converts encoded &#92;uxxxx to unicode chars
     * 
     * and changes special saved chars to their original forms
     */

    public static String fromUnicode(char[] in, int off, int len, char[] convtBuf) {

        if (convtBuf.length < len) {

            int newLen = len * 2;

            if (newLen < 0) {

                newLen = Integer.MAX_VALUE;

            }

            convtBuf = new char[newLen];

        }

        char aChar;

        char[] out = convtBuf;

        int outLen = 0;

        int end = off + len;

        while (off < end) {

            aChar = in[off++];

            if (aChar == '\\') {

                aChar = in[off++];

                if (aChar == 'u') {

                    // Read the xxxx

                    int value = 0;

                    for (int i = 0; i < 4; i++) {

                        aChar = in[off++];

                        switch (aChar) {

                            case '0':

                            case '1':

                            case '2':

                            case '3':

                            case '4':

                            case '5':

                            case '6':

                            case '7':

                            case '8':

                            case '9':

                                value = (value << 4) + aChar - '0';

                                break;

                            case 'a':

                            case 'b':

                            case 'c':

                            case 'd':

                            case 'e':

                            case 'f':

                                value = (value << 4) + 10 + aChar - 'a';

                                break;

                            case 'A':

                            case 'B':

                            case 'C':

                            case 'D':

                            case 'E':

                            case 'F':

                                value = (value << 4) + 10 + aChar - 'A';

                                break;

                            default:

                                throw new IllegalArgumentException(

                                "Malformed \\uxxxx encoding.");

                        }

                    }

                    out[outLen++] = (char) value;

                } else {

                    if (aChar == 't') {

                        aChar = '\t';

                    } else if (aChar == 'r') {

                        aChar = '\r';

                    } else if (aChar == 'n') {

                        aChar = '\n';

                    } else if (aChar == 'f') {

                        aChar = '\f';

                    }

                    out[outLen++] = aChar;

                }

            } else {

                out[outLen++] = (char) aChar;

            }

        }

        return new String(out, 0, outLen);

    }
}
