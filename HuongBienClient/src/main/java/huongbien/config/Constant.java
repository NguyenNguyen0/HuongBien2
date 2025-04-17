package huongbien.config;

import lombok.Getter;

@Getter
public enum Constant {
    VAT(0.1);

    private final double value;

    Constant(double value) {
        this.value = value;
    }
}
