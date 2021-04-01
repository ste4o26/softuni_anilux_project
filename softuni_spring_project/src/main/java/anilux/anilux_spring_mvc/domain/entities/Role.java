package anilux.anilux_spring_mvc.domain.entities;

import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity(name = "roles")
@Table(name = "roles")
public class Role extends BaseEntity {

    @EqualsAndHashCode.Include
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName authority;
}
