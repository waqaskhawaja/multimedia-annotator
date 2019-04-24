/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataDetailComponent } from 'app/entities/data/data-detail.component';
import { Data } from 'app/shared/model/data.model';

describe('Component Tests', () => {
    describe('Data Management Detail Component', () => {
        let comp: DataDetailComponent;
        let fixture: ComponentFixture<DataDetailComponent>;
        const route = ({ data: of({ data: new Data(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DataDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.data).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
