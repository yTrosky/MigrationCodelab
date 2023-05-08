package com.google.samples.apps.sunflower.utilities.databinding;

import androidx.databinding.MergedDataBinderMapper;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.google.samples.apps.sunflower.DataBinderMapperImpl());
  }
}
