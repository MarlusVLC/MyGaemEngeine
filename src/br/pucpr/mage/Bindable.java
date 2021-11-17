package br.pucpr.mage;

public interface Bindable<T> {
    T bind();
    T unbind();
}
