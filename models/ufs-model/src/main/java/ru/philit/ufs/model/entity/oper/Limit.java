package ru.philit.ufs.model.entity.oper;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Limit implements Serializable {

  private String userLogin;
  private boolean tobeIncreased;
  private BigDecimal amount;
  private boolean limitStatusType;

}
