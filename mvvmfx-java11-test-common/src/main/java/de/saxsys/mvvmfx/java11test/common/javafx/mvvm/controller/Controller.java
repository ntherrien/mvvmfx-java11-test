

package de.saxsys.mvvmfx.java11test.common.javafx.mvvm.controller;

public interface Controller<ViewModel extends de.saxsys.mvvmfx.ViewModel> {
    void initialize(ViewModel viewModel);
}
