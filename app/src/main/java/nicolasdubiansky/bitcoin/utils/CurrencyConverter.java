package nicolasdubiansky.bitcoin.utils;

/**
 * Created by Nicolas on 2/10/2017.
 */

public class CurrencyConverter {
    public static final Double ONE_BICTOIN_IN_SHATOSHIS = 100000000.0;

    public static Double shatoshiToBitcoin(Integer shatoshis) {
        return Double.valueOf(shatoshis) / ONE_BICTOIN_IN_SHATOSHIS;

    }

    public static Double shatoshiToBitcoin(Long shatoshis) {
        return Double.valueOf(shatoshis) / ONE_BICTOIN_IN_SHATOSHIS;

    }
}
