package huongbien;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static huongbien.util.Util.suggestMoneyReceived;

public class TestUtil {

    @Test
    public void testSuggestMoneyReceived() {
        // Test case 1: totalAmount is less than 0
        assertThrows(IllegalArgumentException.class, () -> {
            suggestMoneyReceived(-1);
        });

        // Test case 2: totalAmount is 0
        assertThrows(IllegalArgumentException.class, () -> {
            suggestMoneyReceived(0);
        });

        // Test case 3: totalAmount is 1000
        assertArrayEquals(new int[]{1000}, suggestMoneyReceived(1000));

        // Test case 4: totalAmount is 1500
        assertArrayEquals(new int[]{2000, 1000, 2000}, suggestMoneyReceived(1500));

        // Test case 5: totalAmount is 7500
        assertArrayEquals(new int[]{8000, 1000, 2000, 5000, 10000}, suggestMoneyReceived(7500));

        // Test case 6: totalAmount is 123456
        assertArrayEquals(new int[]{124000, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000}, suggestMoneyReceived(123456));
    }
}