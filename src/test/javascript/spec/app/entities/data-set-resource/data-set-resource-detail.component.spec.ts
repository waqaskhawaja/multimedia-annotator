import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { DataSetResourceDetailComponent } from 'app/entities/data-set-resource/data-set-resource-detail.component';
import { DataSetResource } from 'app/shared/model/data-set-resource.model';

describe('Component Tests', () => {
    describe('DataSetResource Management Detail Component', () => {
        let comp: DataSetResourceDetailComponent;
        let fixture: ComponentFixture<DataSetResourceDetailComponent>;
        const route = ({ data: of({ dataSetResource: new DataSetResource(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [DataSetResourceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DataSetResourceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataSetResourceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.dataSetResource).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
