package tsk.teplocounter.pro;

public class RequestsHandling {
    //commands to teplocounter
    int stopbyte = 0b11111111;
    int q1 = 0b10000000; // flow rate in the first pipe
    int q2 = 0b10000001; // flow rate in the second pipe
    int t1 = 0b10000010; // temperature in the first pipe
    int t2 = 0b10000011; // temperature in the second pipe
    int t3 = 0b10000100; // temperature in the third pipe
    int dt = 0b10000101; // difference between temperatures
    int p  = 0b10000110; // thermal power consumption
    int e  = 0b10000111; // amount of heat
    int v1 = 0b10001000; // volume in the first pipe
    int v2 = 0b10001001; // volume in the second pipe
    int t  = 0b10001010; // time
    int d  = 0b10001011; // date
    int h  = 0b10001100; // teplocount time of work in <Work> and <Cont> mode


    public static int[] serialNumber(int number){
        String stringSerialNumber = Integer.toBinaryString(number);
        String[] arrayNumber = stringSerialNumber.split("");
        String preformNumberStr[] = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        int h = preformNumberStr.length - 1;
        for(int d = arrayNumber.length-1; d>=0; d--){
            preformNumberStr[h--] = arrayNumber[d];
        }
        int firstByte = Integer.parseInt("11"+ preformNumberStr[0] + preformNumberStr[1] + preformNumberStr[2] + preformNumberStr[3] + preformNumberStr[4] + preformNumberStr[5], 2);
        int secondByte = Integer.parseInt("0" + preformNumberStr[6] + preformNumberStr[7] + preformNumberStr[8] + preformNumberStr[9] + preformNumberStr[10] + preformNumberStr[11] + preformNumberStr[12], 2);
        int thirdByte = Integer.parseInt("0" + preformNumberStr[13] + preformNumberStr[14] + preformNumberStr[15] + preformNumberStr[16] + preformNumberStr[17] + preformNumberStr[18] + preformNumberStr[19], 2);
        int[] reqNumber = {firstByte, secondByte, thirdByte};
        return reqNumber;
    }

}
