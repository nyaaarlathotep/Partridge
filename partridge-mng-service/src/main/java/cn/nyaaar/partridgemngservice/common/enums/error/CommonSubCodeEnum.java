package cn.nyaaar.partridgemngservice.common.enums.error;


/**
 * @author linhaoran
 * @since 2022/4/23 2:51
 */
public enum CommonSubCodeEnum implements ISubCodeEnum {

  PARAM_PARSE_ERROR("C9999996", "Parameters parse error." ),
  PARAM_NOT_VALID("C9999997", "Parameters not valid."),
  ERROR_OCCURRED("C9999998", "Error occurred."),
  SYSTEM_BUSY("C9999999", "System is busy."),

  DB_ERROR_00000("DB900000", "Error occurred while saving data."),
  DB_ERROR_00001("DB900001", "Business Value exists."),
  DB_ERROR_01438("DB901438", "Some values is too larger."),
  DB_ERROR_00904("DB900904", "Some columns not exists."),

  ;

  private final String subCode;
  private final String subMsg;

  CommonSubCodeEnum(String subCode, String subMsg) {
    this.subCode = subCode;
    this.subMsg = subMsg;
  }

  @Override
  public String getSubCode() {
    return subCode;
  }

  @Override
  public String getSubMsg() {
    return subMsg;
  }
}
