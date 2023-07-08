package springworkflow.tasks.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerNumberingMapper {
	public int find();
	public boolean increment();
}
