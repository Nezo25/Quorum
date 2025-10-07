package pack.treefrog.system.spring.security.qourum.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity (name = "role")
@Table (name = "roles")
@Getter
@Setter
public class Role {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "role_id")
      private  Long roleId;
      private  String name;

      public enum Values  {


          BASIC(2L),
          ADMIN(1L);

          long roleId;
             Values(long roleId) {
                 this.roleId = roleId;
             }

      }

}
