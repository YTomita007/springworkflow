package springworkflow.tasks.form;

import java.io.Serializable;

import org.thymeleaf.util.StringUtils;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindForm implements Serializable {

	private String custCode;

	private String telNo;

	@AssertTrue(message = "得意先コード、電話番号のどちらかを入力してください")
	public boolean isTextEmpty() {
		if (StringUtils.isEmpty(custCode) && StringUtils.isEmpty(telNo)) {
			return false;
		}
		return true;
	}
}
