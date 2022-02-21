package ru.philit.ufs.model.entity.oper;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public enum CashOrderType {

  KO_1("KO1", "КО1"),
  KO_2("KO2", "КО1");

  private static final ImmutableMap<String, CashOrderType> CODES_MAP;

  static {
    Map<String, CashOrderType> mapCodes = new HashMap<>();
    for (CashOrderType item : values()) {
      mapCodes.put(item.code(), item);
    }
    CODES_MAP = ImmutableMap.copyOf(mapCodes);
  }

  public static CashOrderType getByCode(String code) {
    return CODES_MAP.get(code);
  }

  private final String code;
  private final String value;

  CashOrderType(String code, String value) {
    this.code = code;
    this.value = value;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("code", code()).add("value", value()).toString();
  }

  public String code() {
    return code;
  }

  public String value() {
    return value;
  }
}
