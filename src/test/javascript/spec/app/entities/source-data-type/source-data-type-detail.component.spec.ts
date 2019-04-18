/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { SourceDataTypeDetailComponent } from 'app/entities/source-data-type/source-data-type-detail.component';
import { SourceDataType } from 'app/shared/model/source-data-type.model';

describe('Component Tests', () => {
    describe('SourceDataType Management Detail Component', () => {
        let comp: SourceDataTypeDetailComponent;
        let fixture: ComponentFixture<SourceDataTypeDetailComponent>;
        const route = ({ data: of({ sourceDataType: new SourceDataType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [SourceDataTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SourceDataTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SourceDataTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.sourceDataType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
