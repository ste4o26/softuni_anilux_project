package anilux.anilux_spring_mvc.base;

import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import org.junit.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BaseTest {
    @Autowired
    public ValidatorUtil validatorUtil;

    @Autowired
    public ModelMapper modelMapper;

    @Autowired
    public CollectionMapperUtil collectionMapperUtil;

}
