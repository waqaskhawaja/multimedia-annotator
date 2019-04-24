/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataTypeDetailComponent } from 'app/entities/data-type/data-type-detail.component';
import { DataType } from 'app/shared/model/data-type.model';

describe('Component Tests', () => {
    describe('DataType Management Detail Component', () => {
        let comp: DataTypeDetailComponent;
        let fixture: ComponentFixture<DataTypeDetailComponent>;
        const route = ({ data: of({ dataType: new DataType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DataTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.dataType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
