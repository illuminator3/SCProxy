package me.illuminator3.scproxy;

@FunctionalInterface
public interface Converter<T>
{
    T convert();
}