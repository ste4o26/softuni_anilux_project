package anilux.anilux_spring_mvc.domain.view_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenreViewModel extends BaseViewModel {
    private String name;

    @Override
    public String toString(){
        return this.getName();
    }
}
