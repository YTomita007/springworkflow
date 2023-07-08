package springworkflow.tasks.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application implements Serializable {

    private String title;

    private String start;

    private String end;
}

