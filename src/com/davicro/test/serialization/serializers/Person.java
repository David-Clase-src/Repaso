package com.davicro.test.serialization.serializers;

import java.io.Serializable;

public record Person(String name, int age, float money) implements Serializable {
}